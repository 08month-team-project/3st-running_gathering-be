package com.runto.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 유저입니다."),
    USER_INACTIVE(FORBIDDEN, "사용자가 비활성 상태입니다. 이 작업을 수행할 수 없습니다."),

    // 모임글 & 이벤트 관련
    GATHERING_NOT_FOUND(NOT_FOUND, "존재하지 않는 모임글입니다."),
    GATHERING_REPORTED(FORBIDDEN, "신고당한 모임글입니다."),
    EVENT_GATHERING_NOT_APPROVED_ONLY_ORGANIZER_CAN_VIEW(FORBIDDEN, "승인상태가 아닌 이벤트모임은 주최자 본인만 볼 수 있습니다."),
    GENERAL_MAX_NUMBER(BAD_REQUEST, "일반 모임의 최대 인원은 2명에서 10명 사이여야 합니다."),
    EVENT_GATHERING_MAX_NUMBER(BAD_REQUEST, "이벤트 모임의 최대 인원은 10명에서 300명 사이여야 합니다."),
    INVALID_ATTENDANCE_CHECK_NOT_ORGANIZER(FORBIDDEN, "모임의 주최자가 아니면 출석체크 할 수 없습니다."),
    INVALID_ATTENDANCE_CHECK_NOT_NORMAL_GATHERING(FORBIDDEN,"NORMAL 상태가 아닌 모임은 출석체크 할 수 없습니다."),
    INVALID_ATTENDANCE_BEFORE_MEETING(FORBIDDEN, "모임 날짜/시간 전에는 출석체크 할 수 없습니다."),
    INVALID_ATTENDANCE_AFTER_ONE_WEEK(FORBIDDEN, "모임 날짜로부터 일주일이 지난 후에는 출석체크 할 수 없습니다."),
    INVALID_ATTENDANCE_EVENT_GATHERING(BAD_REQUEST, "이벤트 모임은 출석체크를 할 수 없습니다."),
    INVALID_COMPLETE_GATHERING_NOT_ORGANIZER(FORBIDDEN, "모임의 주최자가 아니면 완료시킬 수 없습니다."),
    INVALID_COMPLETE_GATHERING_NOT_NORMAL_GATHERING(FORBIDDEN, "NORMAL 상태가 아닌 모임은 정상완료 할 수 없습니다."),
    INVALID_COMPLETE_GATHERING_BEFORE_MEETING(FORBIDDEN, "모임 날짜/시간 전에는 정상완료할 수 없습니다."),
    INVALID_COMPLETE_AFTER_ONE_WEEK(FORBIDDEN, "모임 날짜로부터 일주일이 지난 후에는 정상완료 할 수 없습니다."),
    INVALID_COMPLETE_UNCHECKED_MEMBERS(FORBIDDEN,"출석체크하지 않은 멤버가 있으면 정상완료를 할 수 없습니다."),
    INVALID_COMPLETE_EVENT_GENERAL_USER(FORBIDDEN,"이벤트 모임은 일반회원이 정상완료시킬 수 없습니다."),


    // 채팅관련,
    CHATROOM_ALREADY_EXIST(CONFLICT, "이미 존재하는 채팅방입니다."),
    CHATROOM_NOT_FOUND(NOT_FOUND, "존재하지 않는 채팅방입니다."),
    CHATROOM_ALREADY_JOINED(BAD_REQUEST, "이미 참여중인 채팅방입니다."),
    CHATROOM_FULL(BAD_REQUEST, "채팅방이 최대 인원수에 도달했습니다."),
    ALREADY_EXIST_USER(CONFLICT, "이미 존재하는 사용자입니다."),
    CHATROOM_CREATE_FAILED_OWN(INTERNAL_SERVER_ERROR, "나 자신과의 채팅방을 만들 수 없습니다."),


    // 이미지 관련
    IMAGE_SAVE_LIMIT_EXCEEDED(BAD_REQUEST, "이미지 저장 허용 개수를 넘었습니다."),
    IMAGE_SERVER_ERROR(INTERNAL_SERVER_ERROR, "이미지 처리 중 내부 오류가 발생했습니다."),
    S3_OBJECT_NOT_FOUND(NOT_FOUND, "S3 객체를 찾을 수 없습니다."),
    UNSUPPORTED_IMAGE_EXTENSION(BAD_REQUEST, "지원하는 이미지 확장자가 아닙니다."),
    INVALID_FILE(BAD_REQUEST, "파일이 없거나 이름이 비어 있습니다."),
    IMAGE_CONVERSION_FAILED(INTERNAL_SERVER_ERROR, "이미지 변환에 실패했습니다."),
    IMAGE_ORDER_MISMATCH(BAD_REQUEST, "요청한 이미지 개수와 순서 개수가 일치하지 않습니다."),
    INVALID_REPRESENTATIVE_IMAGE_INDEX(BAD_REQUEST, "대표 이미지 인덱스가 유효하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}

