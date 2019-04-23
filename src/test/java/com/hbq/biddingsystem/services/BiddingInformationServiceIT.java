package com.hbq.biddingsystem.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbq.biddingsystem.SimpleKafkaTest;
import com.hbq.biddingsystem.WebSocketTest;
import com.hbq.biddingsystem.dtos.BiddingInformationDto;
import com.hbq.biddingsystem.entities.*;
import com.hbq.biddingsystem.repository.BiddingInformationRepository;
import com.hbq.biddingsystem.services.impl.PrepareDataForTest;
import com.hbq.biddingsystem.utils.OurMapper;
import lombok.val;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hungbang on 23/04/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class BiddingInformationServiceIT {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SimpleKafkaTest.class);
    private static final String TOPIC = "topic-bid";

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka =
            new EmbeddedKafkaRule(1, true, TOPIC);

    private Consumer<String, String> consumer;
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private PrepareDataForTest prepareDataForTest;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OurMapper ourMapper;

    @Autowired
    private BiddingInformationRepository biddingInformationRepository;

    @Autowired
    private BiddingInformationService biddingInformationService;

    @Autowired
    private KafkaConsumeService kafkaConsumeService;

    static final String WEBSOCKET_TOPIC = "/topic/updateBid";

    BlockingQueue<String> blockingQueue;
    public WebSocketStompClient stompClient;

    String URL;
    private List<User> users;
    private AuctionCampaign auctionCampaign;
    private List<Product> products;
    String WEBSOCKET_URI = null;
    StompSession session;
    private void initSocket() throws InterruptedException, ExecutionException, TimeoutException {
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        WEBSOCKET_URI = "ws://localhost:" + port + "/webSocket";
         session = stompClient
                .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);
        session.subscribe(WEBSOCKET_TOPIC, new BiddingStompFrameHandler());

    }

    @Before
    public void before() throws InterruptedException, ExecutionException, TimeoutException {
        // prepare data for test
        users = prepareDataForTest.createUsers();
        products = prepareDataForTest.createProducts();
        auctionCampaign = prepareDataForTest.createAuctionCampaigns(products.get(0), users.stream().filter(user -> user.getType().equals(UserType.AUCNEER))
        .findFirst().get());
        // prepare kafka config before test
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafka.getEmbeddedKafka()));
        consumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
        consumer.subscribe(Collections.singleton(TOPIC));
        consumer.poll(0);
        //prepare socket for test
        initSocket();
    }

    @After
    public void tearDown() {
        consumer.close();
    }

    @Test
    public void addBid() throws Exception {
        //GIVEN
        final BiddingInformation biddingInformation = BiddingInformation.builder().biddingPrice(BigDecimal.TEN)
                .auctionCampaign(auctionCampaign)
                .biddingPrice(BigDecimal.valueOf(2))
                .bidder(users.stream().filter(user -> user.getType().equals(UserType.BIDDER)).findFirst().get())
                .build();

        //WHEN
        val result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/biddingInformations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(biddingInformation)))
                .andReturn();

        //THEN
        val dto = objectMapper.readValue(result.getResponse().getContentAsString(), BiddingInformationDto.class);
        BigDecimal currentPrice = auctionCampaign.getStartPrice();
        if(!CollectionUtils.isEmpty(auctionCampaign.getBiddingInformations())){
            val currentBid = auctionCampaign.getBiddingInformations().stream().min(Comparator.comparing(BiddingInformation::getBiddingPrice));
            currentPrice = currentBid.get().getBiddingPrice();
        }
        assertNotNull("Body should not be null", result.getResponse().getContentAsString());
        assertNotEquals("The new bid price must be greater than the old one", dto.getBiddingPrice(), currentPrice);


        String messageData = blockingQueue.poll(10, SECONDS);
        assertNotNull(messageData);
    }

    @Test
    public void findByCriteria() {
        //TODO: testing to fetch the BiddingInformation based on criteria
    }



    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class BiddingStompFrameHandler implements StompFrameHandler {


        private final Logger logger = LoggerFactory.getLogger(BiddingStompFrameHandler.class);


        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            logger.info("payload type: {} ", stompHeaders.toString());
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQueue.offer(o.toString());
        }
    }
}

