package com.runto.domain.user.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportReason {
    ILLEGAL_CONTENT("불법정보개시"),
    INSULT_OR_ABUSE("욕설/인신공격"),
    OBSCENE_CONTENT("음란성/선정성"),
    COMMERCIAL_PROMOTION("영리목적/홍보성"),
    PERSONAL_INFO_EXPOSURE("개인정보노출"),
    DUPLICATE_CONTENT("같은내용 반복게시"),
    FREQUENT_NO_SHOW("잦은노쇼");

    private final String description;

}
