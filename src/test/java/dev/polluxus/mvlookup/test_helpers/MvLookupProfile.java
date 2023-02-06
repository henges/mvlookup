package dev.polluxus.mvlookup.test_helpers;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class MvLookupProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "mvlookup.shared.secret", "none"
        );
    }
}
