package algonquin.cst2335.cai00060;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

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

        // initialize to the ViewModel arraylist
        if (messages == null) {
            chatModel.messages.setValue(messages = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                //get all messages from database
                messages.addAll(myDAO.getAllMessages());
                //set adapter
                binding.recycleView.setAdapter(myAdapter);
                //update the RecyclerView
                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
            });
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

        // register as a listener to the MutableLiveData object selectedMessage
        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            if (newMessageValue != null) {
                // newMessageValue is the value to post
                MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
                // show the fragment on screen
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("Doesn't matter")
                        .replace(R.id.fragmentLocation, chatFragment)
                        .commit();
//                FragmentManager fMgr = getSupportFragmentManager();
//                FragmentTransaction tx = fMgr.beginTransaction();
//                tx.add(R.id.fragmentLocation, chatFragment);
//                tx.commit();
            }
        });
    }


    public class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();

                // click message to show details of it
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);

                /*
                // click message to delete it
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                        .setTitle("Question:")
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            //delete the message on screen
                            ChatMessage removedMessage = messages.remove(position);
                            myAdapter.notifyItemRemoved(position);

                            //delete the message in database
                            Executor threadA = Executors.newSingleThreadExecutor();
                            threadA.execute(() -> {
                                myDAO.deleteMessage(removedMessage);
                            });

                            //create a Snackbar to show a message
                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {
                                        messages.add(position, removedMessage);
                                        myAdapter.notifyItemInserted(position);
                                        //add the deleted message back in database
                                        Executor threadB = Executors.newSingleThreadExecutor();
                                        threadB.execute(() -> {
                                            myDAO.insertMessage(removedMessage);
                                        });
                                    })
                                    .show();
                        })
                        .setNegativeButton("No", (dialog, cl) -> {
                        })
                        .create().show();
                 */

            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

}

