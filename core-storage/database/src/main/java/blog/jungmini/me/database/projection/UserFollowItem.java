package blog.jungmini.me.database.projection;

public record UserFollowItem(Long userId, String nickname, String profileImageUrl, String introduction) {}
