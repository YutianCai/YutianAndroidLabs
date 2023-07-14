package algonquin.cst2335.cai00060;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import algonquin.cst2335.cai00060.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {

    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m) {
        selected = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);
        binding.messageText.setText(selected.getMessage());
        binding.timeText.setText(selected.getTimeSent());
        binding.sendOrReceive.setText(selected.getIsSentButton() ? "Send" : "Receive");
        binding.databaseText.setText("id = " + selected.id);
        return binding.getRoot();
    }
}
