package com.github.probelog.spike;

import com.intellij.execution.testframework.AbstractTestProxy;
import com.intellij.execution.testframework.TestStatusListener;
import org.jetbrains.annotations.Nullable;

public class MyTestStatusListener extends TestStatusListener {
    @Override
    public void testSuiteFinished(@Nullable AbstractTestProxy root) {
        for (AbstractTestProxy test: root.getAllTests()) {
            if (!test.isPassed() && test.getChildren().isEmpty()) {
                System.out.println("Spike: Test Failed:" + test.getName());
            }
        }
    }
}
