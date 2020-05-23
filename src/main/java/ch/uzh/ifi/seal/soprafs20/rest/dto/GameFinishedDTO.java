package ch.uzh.ifi.seal.soprafs20.rest.dto;


import java.util.List;

public class GameFinishedDTO {

    private List<UserGetDTO> winners;

    public List<UserGetDTO> getWinners() {
        return winners;
    }

    public void setWinners(List<UserGetDTO> winners) {
        this.winners = winners;
    }
}
