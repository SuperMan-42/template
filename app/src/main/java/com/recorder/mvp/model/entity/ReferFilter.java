package com.recorder.mvp.model.entity;

import java.util.List;

/**
 * Created by hpw on 17-11-18.
 */

public class ReferFilter {
    private static final long serialVersionUID = 1L;
    private List<String> trade;
    private List<String> rounds;

    public List<String> getTrade() {
        return trade;
    }

    public void setTrade(List<String> trade) {
        this.trade = trade;
    }

    public List<String> getRounds() {
        return rounds;
    }

    public void setRounds(List<String> rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        return "ReferFilter [trade=" + trade + ", rounds=" + rounds + "]";
    }
}
