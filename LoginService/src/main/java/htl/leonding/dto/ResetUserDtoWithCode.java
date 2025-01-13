package htl.leonding.dto;

public record ResetUserDtoWithCode(String username, int RecoveryCode, String password) {
}
