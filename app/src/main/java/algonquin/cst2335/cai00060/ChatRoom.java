package algonquin.cst2335.cai00060;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cai00060.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.cai00060.databinding.ReceiveMessageBinding;
import algonquin.cst2335.cai00060.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    protected ActivityChatRoomBinding binding;
    protected ArrayList<ChatMessage> messages;
    protected ChatRoomViewModel chatModel;
    protected RecyclerView.Adapter myAdapter;
    protected ChatMessageDAO myDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        // initialize myAdapter
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            public int getItemViewType(int position) {
                ChatMessage obj = messages.get(position);
                if (obj.getIsSentButton() == true) return 0;
                else return 1;
            }
        };

        //access the database
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MyChatMessageDatabase").build();
        myDAO = db.cmDAO();

//        if(messages == null) {
//            chatModel.messages.setValue(messages = new ArrayList<>());
//            Executor thread = Executors.newSingleThreadExecutor();
//            thread.execute(() -> {
//                messages.addAll( myDAO.getAllMessages() ); //Once you get the data from database
//                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
//            });
//        }

        //get all messages from database
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            // run on a second processor
            List<ChatMessage> fromDatabase = myDAO.getAllMessages();
            messages.addAll(fromDatabase);
            //set adapter
            binding.recycleView.setAdapter(myAdapter);
            // update the recycler view
            runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
        });

        // initialize to the ViewModel arraylist
        messages = chatModel.messages.getValue();
        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());
        }

        binding.sendButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage newChatMessage = new ChatMessage(binding.textInput.getText().toString(), currentDateAndTime, true);
            //insert into ArrayList
            messages.add(newChatMessage);
            //insert into database. It returns the id of this new insertion
            Executor threadA = Executors.newSingleThreadExecutor();
            threadA.execute(() -> {
                // run on a second processor
                newChatMessage.id = myDAO.insertMessage(newChatMessage);
            });
            //update the rows
            myAdapter.notifyItemInserted(messages.size() - 1);
            // myAdapter.notifyDataSetChanged();

            //clear the previous text
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage newChatMessage = new ChatMessage(binding.textInput.getText().toString(), currentDateAndTime, false);
            //insert into ArrayList
            messages.add(newChatMessage);
            //insert into database. It returns the id of this new insertion
            Executor threadA = Executors.newSingleThreadExecutor();
            threadA.execute(() -> {
                // run on a second processor
                newChatMessage.id = myDAO.insertMessage(newChatMessage);
            });
            //update the rows
            myAdapter.notifyItemInserted(messages.size() - 1);
            //clear the previous text
            binding.textInput.setText("");
        });

        // To specify a single column scrolling in a Vertical direction
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

    }


    public class MyRowHolder extends RecyclerView.ViewHolder {
        ArrayList<String> messages = new ArrayList<>();
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

}

