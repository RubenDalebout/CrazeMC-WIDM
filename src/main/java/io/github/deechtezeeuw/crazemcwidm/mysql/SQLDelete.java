package io.github.deechtezeeuw.crazemcwidm.mysql;

import io.github.deechtezeeuw.crazemcwidm.CrazeMCWIDM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLDelete {
    private final CrazeMCWIDM plugin = CrazeMCWIDM.getInstance();

    // Delete contestant
    public void deleteContestant(UUID contestant) {
        if (plugin.getSQL().sqlSelect.contestantExists(contestant)) {
            try {
                PreparedStatement ps = plugin.getSQL().getConnection().prepareStatement("DELETE FROM widm_contestants WHERE UUID=?");
                ps.setString(1, contestant.toString());

                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Delete game
    public void deleteGame(UUID game) {
        if (plugin.getSQL().sqlSelect.gameExists(game)) {
            try {
                PreparedStatement ps = plugin.getSQL().getConnection().prepareStatement("DELETE FROM widm_games WHERE UUID=?");
                ps.setString(1, game.toString());

                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
