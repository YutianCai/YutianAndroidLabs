package algonquin.cst2335.cai00060;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Yutian Cai
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * This holds the text at the center of the screen.
     */
    TextView tv = null;
    /**
     * This holds the edit text area under TextView.
     */
    EditText et = null;
    /**
     * This holds the button at the bottom of the screen.
     */
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            if (checkPasswordComplexity(password)) {
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }
        });


    }

    /**
     * This function check the complexity of the passed password.
     *
     * @param pw The String object that we are checking
     * @return true if the passed string has an Upper Case letter, a lower case letter,
     * a number, and a special symbol (#$%^&*!@?); false otherwise.
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        // iterate over each character in the String,and check if a character matches any of those conditions
        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        // check that each if the variables have been set to true
        if (!foundUpperCase) {
            // Say that they are missing an upper case letter;
            Toast.makeText(this, "Missing an upper case letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundLowerCase) {
            // Say that they are missing a lower case letter;
            Toast.makeText(this, "Missing a lower case letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundNumber) {
            Toast.makeText(this, "Missing a number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(this, "Missing a special character", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true; //only get here if they're all true
    }

    /**
     * This function check if the passed character is a special character.
     *
     * @param c The character that we are checking
     * @return true if the passed character is one of these special character ( #$%^&*!@? ); false otherwise.
     */
    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }

}