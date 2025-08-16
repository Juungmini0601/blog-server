package org.logly.database.projection;

public record UserFollowItem(Long userId, String nickname, String profileImageUrl, String introduction) {}
