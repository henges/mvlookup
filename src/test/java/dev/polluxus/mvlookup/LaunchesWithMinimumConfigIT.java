package dev.polluxus.mvlookup;

import dev.polluxus.mvlookup.LaunchesWithMinimumConfigIT.LaunchesWithMinimumConfigTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import java.util.Map;

@QuarkusTest
@TestProfile(LaunchesWithMinimumConfigTestProfile.class)
public class LaunchesWithMinimumConfigIT {

    /**
     * Demonstrates that the application will launch
     * with at minimum the below configuration values set.
     */
    @Test
    public void test() { }

    public static class LaunchesWithMinimumConfigTestProfile implements QuarkusTestProfile {

        @Override
        public Map<String, String> getConfigOverrides() {

            return Map.of(
                    "mvlookup.bot.url", "FAKE_VALUE",
                    "mvlookup.bot.token", "FAKE_VALUE",
                    "tmdb.api.token", "FAKE_VALUE"
            );
        }
    }
}
