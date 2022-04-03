import ReactionPoll.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bot {

    public static JDA jda;
    public static JDABuilder builder;

    public static void main(String[] args) throws LoginException, IOException {
        builder = JDABuilder.createDefault("OTU5OTExMDMwNzkwNjg4NzY5.Ykiw0A.JRTq7M70PLsk-A3mzcF-PS-plVg");
        builder.addEventListeners(new Command());
        jda = builder.build();

        System.out.println("[Bot] online!");

        stop();
    }

    public static void stop() throws IOException {
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            if (reader.readLine().equals("stop")) {
                reader.close();
                builder.setStatus(OnlineStatus.OFFLINE);
                jda.shutdown();
                System.out.println("[Bot] offline");
                System.exit(1);
            }
        }
    }
}
