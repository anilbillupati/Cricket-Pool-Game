package com.CricketBetting.Cricket.mapper;

import com.CricketBetting.Cricket.domain.entity.WalletTransaction;
import com.CricketBetting.Cricket.domain.response.WalletTransactionResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletTransactionMapper {
    WalletTransactionResponseDto toDto(WalletTransaction transaction);

    List<WalletTransactionResponseDto> toResponseList(List<WalletTransaction> transactions);

}
