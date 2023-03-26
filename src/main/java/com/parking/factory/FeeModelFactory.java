package com.parking.factory;

import com.parking.components.feemodel.IFeeModel;
import com.parking.constants.FeeModelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public final class FeeModelFactory {

    private static Map<FeeModelType, IFeeModel> feeModelMap;

    @Autowired
    private FeeModelFactory(final List<IFeeModel> feeModels) {
        feeModelMap = feeModels.stream().collect(
                Collectors.toUnmodifiableMap(IFeeModel::getType, Function.identity()));
    }

    public static IFeeModel getFeeModel(final FeeModelType feeModelType) {
        return Optional.ofNullable(feeModelMap.get(feeModelType)).orElseThrow(IllegalArgumentException::new);
    }
}
