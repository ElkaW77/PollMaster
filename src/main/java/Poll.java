import lombok.AllArgsConstructor;
import lombok.Data;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.Event;

import java.util.List;

@Data
@AllArgsConstructor
public class Poll {
    private int id;
    private String mId;
    private String question;
    private float votes;
    private List<Option> options;
    MessageEmbed message;

    public void vote(int id) {
        for (Option opt:
             options) {
            if(opt.getId() == id) {
                opt.setVotes(opt.getVotes() + 1);
                this.votes++;
            }
        }

        for (Option opt:
             options) {
            if (opt.getVotes() != 0) {
                opt.setPercentage(opt.getVotes() / this.votes);
            }
        }
    }
}
