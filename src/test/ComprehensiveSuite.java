package test;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"test.utils", "test.engine"})
public class ComprehensiveSuite {

}
