package algonquin.cst2335.cai00060;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "timeSent")
    private String timeSent;

    @ColumnInfo(name = "isSentButton")
    private boolean isSentButton;

    public ChatMessage() {
    }

    public ChatMessage(String m, String t, boolean sent) {
        setMessage(m);
        setTimeSent(t);
        setIsSentButton(sent);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public boolean getIsSentButton() {
        return isSentButton;
    }

    public void setIsSentButton(boolean isSentButton) {
        this.isSentButton = isSentButton;
    }
}
