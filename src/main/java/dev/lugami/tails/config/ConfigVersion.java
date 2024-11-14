package dev.lugami.tails.config;

import dev.lugami.tails.config.impl.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigVersion {

    VERSION_1(1, new ConfigConversion1()),
    VERSION_2(2, new ConfigConversion2()),
    VERSION_3(3, new ConfigConversion3()),
    VERSION_4(4, new ConfigConversion4()),
    VERSION_5(5, new ConfigConversion5());

    private int number;
    private ConfigConversion conversion;

}