package com.memesAndPunkRock.fakeInst.ml;

import com.memesAndPunkRock.fakeInst.api.data.UserData;

import java.util.List;

public interface CustomModelController {
    List<String> getPredictions(UserData user);
}
