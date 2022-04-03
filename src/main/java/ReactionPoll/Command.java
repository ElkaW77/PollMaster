package ReactionPoll;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Command extends ListenerAdapter {

    List<Poll> polls = new ArrayList<>();
    int n = 0;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentStripped().split(",");

        if (event.getMessage().getContentStripped().startsWith("poll")) {

            //If there are less than 2 or more than 10 options
            if(args.length < 4 || args.length > 12){
                event.getChannel().sendMessageEmbeds(errorEmbed()).queue();
                return;
            }

            List<Option> optionList = new ArrayList<>();
            for (int i = 2; i < args.length; i++) {
                optionList.add(
                        new Option( i - 2, args[i], 0, 0)
                );
            }
            Poll poll = new Poll(n, null, args[1], 0, optionList, null);
            this.polls.add(poll);
            n++;

            MessageEmbed message = pollEmbed(poll);
            poll.setMessage(message);
            event.getChannel().sendMessageEmbeds(message).queue(m -> {
                for (Option opt :
                        poll.getOptions()) {
                    m.addReaction(opt.getEmoji()).queue();
                }
                poll.setMId(m.getId());
            });
        }
    }



    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if(!event.getUserId().equals("959911030790688769")) {
            System.out.println("reaction");
            String emoji = event.getReactionEmote().getEmoji();
            int optId = this.getIdByEmoji(emoji);
            Poll poll = polls.stream().filter(p -> p.getMId().equals(event.getMessageId())).findAny().get();
            polls.remove(poll);
            poll.vote(optId, 1);
            polls.add(poll);

            event.getChannel().editMessageEmbedsById(poll.getMId(), pollEmbed(poll)).queue();
        }
    }

    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        if(!event.getUserId().equals("959911030790688769")) {
            String emoji = event.getReactionEmote().getEmoji();
            int optId = this.getIdByEmoji(emoji);
            Poll poll = polls.stream().filter(p -> p.getMId().equals(event.getMessageId())).findAny().get();
            polls.remove(poll);
            poll.vote(optId, -1);
            polls.add(poll);

            event.getChannel().editMessageEmbedsById(poll.getMId(), pollEmbed(poll)).queue();
        }
    }

    private MessageEmbed pollEmbed(Poll poll){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.cyan);
        builder.setTitle("Poll Id: " + poll.getId());
        builder.setDescription(poll.getQuestion());
        poll.getOptions().forEach(opt ->
                builder.addField(opt.getEmoji() + opt.getText(), this.percentage(opt), false));

        return builder.build();
    }

    private MessageEmbed errorEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.RED);
        builder.setTitle("Error");
        builder.setDescription("Mindestens 2 und maximal 10 Auswahlmöglichkeiten!");
        return builder.build();
    }

    private String percentage(Option opt){
        if(opt.getPercentage() == 0){
            return ":white_large_square:".repeat(26);
        }

        float i = 1;
        while((i / 26) <= opt.getPercentage() && i <= 26) {
            i++;
        }

        StringBuilder percentage = new StringBuilder();
        for (float j = 0; j < i - 1; j++) {
            percentage.append(":green_square:");
        }
        for (float j = i; j <= 26; j++) {
            percentage.append(":white_large_square:");
        }
        return percentage.toString();
    }

    public int getIdByEmoji(String emoji) {
        switch (emoji) {
            case "0️⃣": return 0;
            case "1️⃣": return 1;
            case "2️⃣": return 2;
            case "3️⃣": return 3;
            case "4️⃣": return 4;
            case "5️⃣": return 5;
            case "6️⃣": return 6;
            case "7️⃣": return 7;
            case "8️⃣": return 8;
            case "9️⃣": return 9;
            case "\uD83D\uDD1F": return 10;
        }
        return 11;
    }
}
