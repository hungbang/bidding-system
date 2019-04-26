package com.hbq.biddingsystem.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbq.biddingsystem.dtos.BiddingInformationDto;
import com.hbq.biddingsystem.entities.*;
import com.hbq.biddingsystem.repository.BiddingInformationRepository;
import com.hbq.biddingsystem.services.impl.PrepareDataForTest;
import lombok.val;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.*;
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
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.util.NestedServletException;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

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
            LoggerFactory.getLogger(BiddingInformationServiceIT.class);
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
    private BiddingInformationRepository biddingInformationRepository;

    static final String WEBSOCKET_TOPIC = "/topic/updateBid";

    BlockingQueue<String> blockingQueue;

    private List<User> users;
    private AuctionCampaign auctionCampaign;
    private List<Product> products;
    StompSession session;

    private void initSocket() throws InterruptedException, ExecutionException, TimeoutException {
        final String URL = "ws://localhost:" + port + "/webSocket";
        blockingQueue = new LinkedBlockingDeque<>();
        final List<Transport> transportList =
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
        final WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transportList));
        stompClient.setMessageConverter(new StringMessageConverter());
        session = stompClient.connect(URL, new CustomStompSessionHandlerAdapter()).get(5, SECONDS);

    }

    @Before
    public void before() throws InterruptedException, ExecutionException, TimeoutException {
        // prepare data for test
        users = prepareDataForTest.createUsers();
        products = prepareDataForTest.createProducts();
        auctionCampaign = prepareDataForTest.createAuctionCampaigns(products.get(0),
                users.stream().filter(user -> user.getType().equals(UserType.AUCNEER))
                        .findFirst().get());
        // prepare kafka config before test
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false",
                embeddedKafka.getEmbeddedKafka()));
        consumer =
                new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
        consumer.subscribe(Collections.singleton(TOPIC));
        consumer.poll(0);
        //prepare socket for test
        initSocket();
    }

    @After
    public void tearDown() {
        consumer.close();
        session.disconnect();
    }

    @Test
    public void addBidShouldBeSuccess() throws Exception {
        //GIVEN
        final BiddingInformation biddingInformation = BiddingInformation.builder().biddingPrice(BigDecimal.TEN)
                .auctionCampaign(auctionCampaign)
                .biddingPrice(BigDecimal.valueOf(2))
                .bidder(users.stream().filter(user -> user.getType().equals(UserType.BIDDER)).findFirst().get())
                .build();

        //WHEN
        val request = mockMvc.perform(MockMvcRequestBuilders.post("/v1/biddingInformations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(biddingInformation)))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        val mockResult = mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(request))
                .andReturn();
        //THEN
        val dto = objectMapper.readValue(mockResult.getResponse().getContentAsString(), BiddingInformationDto.class);
        BigDecimal currentPrice = auctionCampaign.getStartPrice();
        if (!CollectionUtils.isEmpty(auctionCampaign.getBiddingInformations())) {
            val currentBid =
                    auctionCampaign.getBiddingInformations().stream().min(Comparator.comparing(BiddingInformation::getBiddingPrice));
            currentPrice = currentBid.get().getBiddingPrice();
        }
        assertNotNull("Body should not be null", mockResult.getResponse().getContentAsString());
        assertNotEquals("The new bid price must be greater than the old one", dto.getBiddingPrice(), currentPrice);

        String messageData = blockingQueue.poll(10, SECONDS);
        LOGGER.info("======data received : {}", messageData);
        BiddingInformationDto actualData = objectMapper.readValue(messageData, BiddingInformationDto.class);
        assertNotNull(messageData);
        assertEquals("The Bidding price should the same one", biddingInformation.getBiddingPrice(),
                actualData.getBiddingPrice());
        assertEquals("The product should be the same one", biddingInformation.getAuctionCampaign().getProduct().getId(),
                actualData.getAuctionCampaign().getProduct().getId());
    }

    @Test(expected = NestedServletException.class)
    public void addBidShouldBeFailureWhenPriceLower() throws Exception {
        // add first bid
        final BiddingInformation firstBid = BiddingInformation.builder().biddingPrice(BigDecimal.TEN)
                .auctionCampaign(auctionCampaign)
                .biddingPrice(BigDecimal.valueOf(2))
                .bidder(users.stream().filter(user -> user.getType().equals(UserType.BIDDER)).findFirst().get())
                .build();
        biddingInformationRepository.save(firstBid);

        //GIVEN
        final BiddingInformation biddingInformation = BiddingInformation.builder().biddingPrice(BigDecimal.TEN)
                .auctionCampaign(auctionCampaign)
                .biddingPrice(BigDecimal.valueOf(0.2))
                .bidder(users.stream().filter(user -> user.getType().equals(UserType.BIDDER)).findFirst().get())
                .build();

        //WHEN
        val request = mockMvc.perform(MockMvcRequestBuilders.post("/v1/biddingInformations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(biddingInformation)))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        val mockResult = mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(request)).andReturn();
        //THEN
        LOGGER.info("Should be thrown exception", mockResult);
    }


    @Test
    public void test1000BidsPerMinutesSuccess(){

        //GIVEN
        int corePoolSize = 1000;
        int maxPoolSize = 1001;

        long keepAliveTime=0;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                MILLISECONDS, new LinkedBlockingDeque<>());
        for (int i = 1; i <= 1000; i++){
            final int price = i;
            threadPoolExecutor.execute(() -> {
                final BiddingInformation biddingInformation = BiddingInformation.builder().biddingPrice(BigDecimal.TEN)
                        .auctionCampaign(auctionCampaign)
                        .biddingPrice(BigDecimal.valueOf(price))
                        .bidder(users.stream().filter(user -> user.getType().equals(UserType.BIDDER)).findFirst().get())
                        .build();
                try {
                    val result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/biddingInformations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(biddingInformation)))
                            .andExpect(request().asyncStarted())
                            .andDo(MockMvcResultHandlers.log())
                            .andReturn();

                    val mockResult = mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(result)).andReturn();
                    String messageData = blockingQueue.poll(10, SECONDS);
                    LOGGER.info("======data received : {}", messageData);
                    BiddingInformationDto actualData = objectMapper.readValue(messageData, BiddingInformationDto.class);
                    assertNotNull(messageData);
                    assertEquals("The Bidding price should the same one", biddingInformation.getBiddingPrice(),
                            actualData.getBiddingPrice());
                    assertEquals("The product should be the same one", biddingInformation.getAuctionCampaign().getProduct().getId(),
                            actualData.getAuctionCampaign().getProduct().getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//               LOGGER.info("Testing======{}", LocalDateTime.now());
            });
        }
    }


    //** custom code after this line **//

    public class CustomStompSessionHandlerAdapter extends StompSessionHandlerAdapter {


        @Override
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            LOGGER.info("=======Handle Frame with payload: {}", payload);
            try {
                blockingQueue.offer((String) payload, 500, MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe(WEBSOCKET_TOPIC, this);
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                    Throwable exception) {
            LOGGER.warn("Stomp error: ", exception);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            super.handleTransportError(session, exception);
            LOGGER.warn("Stomp Transport Error:", exception);
        }
    }
}

