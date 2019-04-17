package com.hbq.biddingsystem.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class BaseDto {

    protected String id;

    protected LocalDateTime creationDate;

    protected LocalDateTime modifiedDate;

    protected String createdBy;

    protected String modifiedBy;

    protected int version;

}
