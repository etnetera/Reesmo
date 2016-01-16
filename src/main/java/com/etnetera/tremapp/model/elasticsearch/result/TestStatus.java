package com.etnetera.tremapp.model.elasticsearch.result;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TestStatus {

	/**
	 * When a test is run and the actual outcome does not match the expected
	 * out-come.
	 */
	FAILED,

	/**
	 * When a test is run, an error that keeps the test from running to
	 * completion. The error may be explicitly raised or thrown by the system
	 * under test (SUT) or by the test itself, or it may be thrown by the
	 * runtime system (e.g., operating system, virtual machine). In general, it
	 * is much easier to debug a test error than a test failure because the
	 * cause of the problem tends to be much more local to where the test error
	 * occurs. Compare with test failure and test success.
	 */
	BROKEN, 
	
	/**
	 * Test that did not run for some reason, e.g. test dependencies.
	 */
	SKIPPED,

	/**
	 * A situation in which a test is run and all actual outcomes match the
	 * expected outcomes. Compare with test failure and test error.
	 */
	PASSED;
	
	public static List<String> getAsStringList() {
		return Stream.of(values()).map(Enum::name).collect(Collectors.toList());
	}
	
}
