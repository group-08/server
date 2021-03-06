package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.user.UserStatus;

public class UserGetDTO {

    private Long id;
    private String email;
    private String username;
    private UserStatus status;
    private Integer leaderBoardScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Integer getLeaderBoardScore() {
        return leaderBoardScore;
    }

    public void setLeaderBoardScore(Integer leaderBoardScore) {
        this.leaderBoardScore = leaderBoardScore;
    }
}
