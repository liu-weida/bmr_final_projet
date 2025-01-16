package org.rhw.bmr.project.common.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 短链接有效期类型
 */
@RequiredArgsConstructor
public enum ValidDateType {

    PERMANENT(0),
    CUSTOM(1);

    @Getter
    private final int type;
}
