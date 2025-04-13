package com.CricketBetting.Cricket.mapper;

import com.CricketBetting.Cricket.domain.entity.WalletTransaction;
import com.CricketBetting.Cricket.domain.response.WalletTransactionResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletTransactionDtoMapper {
    WalletTransactionResponseDto toDto(WalletTransaction transaction);
}