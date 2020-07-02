package com.github.probelog.intellij;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PopupDialogAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    /*

    Spikes

    - Create

    VFileCreateEvent

    Spike event = VfsEvent[update: file:///Users/dave.halpin/git/bt-pc/usermigrator/src/test/test1.txt]
    Spike canonicalPath = /Users/dave.halpin/git/bt-pc/usermigrator/src/test/test1.txt
    Spike name          = test1.txt
    Spike path          = /Users/dave.halpin/git/bt-pc/usermigrator/src/test/test1.txt
    Spike parent name   = test


    - Update

    VFileContentChangeEvent

    Spike event = VfsEvent[update: file:///Users/dave.halpin/git/bt-pc/usermigrator/src/test/test1.txt]
    Spike canonicalPath = /Users/dave.halpin/git/bt-pc/usermigrator/src/test/test1.txt
    Spike name          = test1.txt
    Spike path          = /Users/dave.halpin/git/bt-pc/usermigrator/src/test/test1.txt
    Spike parent name   = test

    - refactor (rename)

    Spike event = VfsEvent[property(name) changed for 'file:///Users/dave.halpin/git/bt-pc/usermigrator/src/test/test4.txt': test3.txt -> test4.txt]
    Spike canonicalPath = /Users/dave.halpin/git/bt-pc/usermigrator/src/test/test4.txt
    Spike name          = test4.txt
    Spike path          = /Users/dave.halpin/git/bt-pc/usermigrator/src/test/test4.txt
    Spike parent name   = test

    VFilePropertyChangeEvent
    VfsEvent[property(name) changed for 'file:///Users/dave.halpin/git/bt-pc/usermigrator/src/test/test5.txt': test4.txt -> test5.txt]

  public Object getNewValue()
  public Object getOldValue()
  case VirtualFile.PROP_NAME: ----> public static final String PROP_NAME = "name";
  public String getPropertyName()

    - Move
    public class VirtualFileMoveEvent extends VirtualFileEvent {
    public VirtualFile getOldParent()
    public VirtualFile getNewParent()


    - Delete
    VFileDeleteEvent

    also Copy

    public class VFileCopyEvent
  public VirtualFile getNewParent()

     */

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        // this works ;-)

        MessageBusConnection connection = ApplicationManager.getApplication().getMessageBus().connect();

        FileChangeEpisodeBuilder episodeBuilder = new FileChangeEpisodeBuilder();

        connection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {

            @Override
            public void before(@NotNull List<? extends VFileEvent> events) {
                for (VFileEvent event : events) {

                    System.out.println("Spike event before = " + event);
                    VirtualFile file = event.getFile();
                    System.out.println("Spike length before  = " + file.getLength());
                }
            }
            @Override
            public void after(@NotNull List<? extends VFileEvent> events) {
                for (VFileEvent event : events) {
                    // TODO see if there is a Visitor interface i can use here
                    if (event instanceof VFileCreateEvent)  { // tested this - works ok
                        episodeBuilder.create(event.getPath());
                    }

                    System.out.println("Spike event after = " + event);
                    VirtualFile file = event.getFile();
                    System.out.println("Spike length after  = " + file.getLength());
                    System.out.println("Spike canonicalPath = " + file.getCanonicalPath());
                    System.out.println("Spike name          = " + file.getName());
                    System.out.println("Spike path          = " + file.getPath());
                    System.out.println("Spike parent name   = " + file.getParent().getName());
                }
            }
        });

    }

}
