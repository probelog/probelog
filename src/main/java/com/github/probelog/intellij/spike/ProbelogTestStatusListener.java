package com.github.probelog.intellij.spike;

import com.github.probelog.testrun.TestRunBuilder;
import com.github.probelog.ui.spike.TestRunExporter;
import com.intellij.execution.testframework.AbstractTestProxy;
import com.intellij.execution.testframework.TestStatusListener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProbelogTestStatusListener extends TestStatusListener  {

    public static TestRunBuilder testRunBuilder;
    public static TestRunExporter testRunExporter;

    @Override
    public void testSuiteFinished(@Nullable AbstractTestProxy root) {
        if (testRunBuilder==null)
            return;
        List<String> allTests = new ArrayList();
        List<String> failedTests = new ArrayList();
        for (AbstractTestProxy test: root.getAllTests()) {
            if (test.getChildren().isEmpty()) {
                allTests.add(test.getName());
                if (!test.isPassed())
                    failedTests.add(test.getName());
            }
        }
        testRunBuilder.testRun(allTests,failedTests);
        testRunExporter.export();
    }

}
