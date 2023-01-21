package com.promineotech.jeep.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Option {
  private Long optionPK;
  private String optionId;
  private OptionType category;
  private String manufacturer;
  private String name;
  private BigDecimal price;
  
  @JsonIgnore
  public Long getOptionPK() {
  	return optionPK;
  }
}
