package com.github.probelog.spike;

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsAdapter;
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.rt.execution.junit.IDEAJUnitListener;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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


  Test Run Listener

 junit 5 extensions - https://junit.org/junit5/docs/5.4.0-RC2/user-guide/index.html

https://junit.org/junit5/docs/5.4.0-RC2/user-guide/index.html#extensions-registration-automatic
 Package org.junit.jupiter.api.extension
Interface TestWatcher

in plugin.xml there is this

    <extensions defaultExtensionNs="com.intellij">
        <!-- Add your extensions here -->
    </extensions>

     */

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        // this works ;-)

        MessageBusConnection connection = ApplicationManager.getApplication().getMessageBus().connect();

        connection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
            @Override
            public void after(@NotNull List<? extends VFileEvent> events) {
                for (VFileEvent event : events) {
                    System.out.println("Spike event = " + event);
                    VirtualFile file = event.getFile();
                    System.out.println("Spike canonicalPath = " + file.getCanonicalPath());
                    System.out.println("Spike name          = " + file.getName());
                    System.out.println("Spike path          = " + file.getPath());
                    System.out.println("Spike parent name   = " + file.getParent().getName());
                }
            }
        });

        final IDEAJUnitListener junitListener = new IDEAJUnitListener() {
            @Override
            public void testStarted(String s, String s1) {

            }

            @Override
            public void testFinished(String s, String s1) {
                System.out.println("Spike Test run - arg 1: " + s);
                System.out.println("Spike Test run - arg 2: " + s1);
            }
        };

        final SMTRunnerEventsListener smtRunnerEventsListener = new SMTRunnerEventsListener() {
            @Override
            public void onTestingStarted(@NotNull SMTestProxy.SMRootTestProxy smRootTestProxy) {

            }

            @Override
            public void onTestingFinished(@NotNull SMTestProxy.SMRootTestProxy smRootTestProxy) {
                System.out.println("Spike testproxy:" + smRootTestProxy);
                for (SMTestProxy test: smRootTestProxy.getAllTests()) {
                    System.out.println("Spike Test: " + test.getName() + ", Pass: " + test.isPassed());
                }
            }

            @Override
            public void onTestsCountInSuite(int i) {

            }

            @Override
            public void onTestStarted(@NotNull SMTestProxy smTestProxy) {

            }

            @Override
            public void onTestFinished(@NotNull SMTestProxy smTestProxy) {

            }

            @Override
            public void onTestFailed(@NotNull SMTestProxy smTestProxy) {

            }

            @Override
            public void onTestIgnored(@NotNull SMTestProxy smTestProxy) {

            }

            @Override
            public void onSuiteFinished(@NotNull SMTestProxy smTestProxy) {

            }

            @Override
            public void onSuiteStarted(@NotNull SMTestProxy smTestProxy) {

            }

            @Override
            public void onCustomProgressTestsCategory(@Nullable String s, int i) {

            }

            @Override
            public void onCustomProgressTestStarted() {

            }

            @Override
            public void onCustomProgressTestFailed() {

            }

            @Override
            public void onCustomProgressTestFinished() {

            }

            @Override
            public void onSuiteTreeNodeAdded(SMTestProxy smTestProxy) {

            }

            @Override
            public void onSuiteTreeStarted(SMTestProxy smTestProxy) {

            }
        };

        connection.subscribe(SMTRunnerEventsListener.TEST_STATUS, new SMTRunnerEventsAdapter() {
                    @Override
                    public void onTestFinished(@NotNull SMTestProxy test) {
                        System.out.println("Spike Test: " + test.getName() + ", Pass: " + test.isPassed());
                    }
                }
            );

    }

}
