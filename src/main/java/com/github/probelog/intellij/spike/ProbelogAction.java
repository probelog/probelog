package com.github.probelog.intellij.spike;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRunBuilder;
import com.github.probelog.export.html.TestRunExporter;
import com.github.probelog.util.FileUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProbelogAction extends AnAction {

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
        TestRunBuilder testRunBuilder = new TestRunBuilder(episodeBuilder);
        FileUtil fileUtil = new FileUtil("/Users/dave.halpin/git/dave/probelog/testbed/");
        ProbelogTestStatusListener.testRunBuilder = testRunBuilder;
        ProbelogTestStatusListener.testRunExporter = new TestRunExporter(testRunBuilder, fileUtil);


        connection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {

            @Override
            public void before(@NotNull List<? extends VFileEvent> events) {
                for (VFileEvent event : events) {
                    String path = event.getPath();
                    if (path.endsWith(".java")) {
                        if (event instanceof VFileContentChangeEvent) {
                            if (episodeBuilder.isUnknown(path))
                                episodeBuilder.initialize(path, fileUtil.copyToCheckSum(path));
                        }
                    }
                }
            }
            @Override
            public void after(@NotNull List<? extends VFileEvent> events) {
                for (VFileEvent event : events) {
                    String path = event.getPath();
                    if (path.endsWith(".java")) {
                        if (event instanceof VFileCreateEvent) { // tested this - works ok
                            episodeBuilder.create(path);
                        }
                        if (event instanceof VFileContentChangeEvent) {
                            episodeBuilder.update(path, fileUtil.copyToCheckSum(path));
                        }
                    }
                }
            }
        });

    }

}
