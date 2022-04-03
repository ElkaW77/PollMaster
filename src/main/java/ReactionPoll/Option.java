package ReactionPoll;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Option {
    private int id;
    private String text;
    private float votes;
    private float percentage;

    public String getEmoji() {
        switch (id) {
            case 0: return "0️⃣";
            case 1: return "1️⃣";
            case 2: return "2️⃣";
            case 3: return "3️⃣";
            case 4: return "4️⃣";
            case 5: return "5️⃣";
            case 6: return "6️⃣";
            case 7: return "7️⃣";
            case 8: return "8️⃣";
            case 9: return "9️⃣";
            case 10: return "\uD83D\uDD1F";
        }
        return ":zero:";
    }
}
