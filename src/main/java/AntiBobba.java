import gearth.extensions.ExtensionForm;
import gearth.extensions.ExtensionInfo;
import gearth.extensions.extra.tools.PacketInfoSupport;
import gearth.protocol.HMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

@ExtensionInfo(
        Title = "AntiBobba",
        Description = "Bypass for a swear word",
        Version = "V.1",
        Author = "achantur"
)

public class AntiBobba extends ExtensionForm {
    public PacketInfoSupport packetInfoSupport;
    public CheckBox antiBobba;

    public static void main(String[] args) {
        runExtensionForm(args, AntiBobba.class);
    }

    @Override
    public ExtensionForm launchForm(Stage primaryStage) throws Exception {
        primaryStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AntiBobba.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("AntiBobba");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);

        return loader.getController();
    }

    @Override
    protected void initExtension() {
        packetInfoSupport = new PacketInfoSupport(this);

        // Intercept message send to server
        packetInfoSupport.intercept(HMessage.Direction.TOSERVER, "ChatMessageComposer", hMessage -> {
            String message = hMessage.getPacket().readString();
            int color = hMessage.getPacket().readInteger();
            int index = hMessage.getPacket().readInteger();
            if (antiBobba.isSelected()) {
                hMessage.setBlocked(true);
                bypass(message, color, index);
            }
        });
    }

    //bypass for message
    private void bypass(String message, int color, int index) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char ch : message.toCharArray()) {
            stringBuilder.append("ӵӵ");
            stringBuilder.append(ch);
        }
        stringBuilder.append("ӵӵ");
        packetInfoSupport.sendToServer("ChatMessageComposer", stringBuilder.toString(), color, index);
    }
}