/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.asset;

import codex.runner.build.SlopeWeight;
import codex.runner.build.SlopeWeightData;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author codex
 */
public class SlopeWeightLoader implements AssetLoader {

    @Override
    @SuppressWarnings("null")
    public SlopeWeightData load(AssetInfo assetInfo) throws IOException {
        var data = new SlopeWeightData();
        var br = new BufferedReader(new InputStreamReader(assetInfo.openStream()));
        String line;
        SlopeWeight weight = null;
        while ((line = getNextLine(br)) != null) {
            if (line.startsWith("GlobalStep")) {
                weight = new SlopeWeight(getTrailingInteger(line));
                data.addLast(weight);
            }
            else if (line.startsWith("weight")) {
                weight.setWeight(getTrailingInteger(line));
            }
        }
        return data;
    }
    
    private String getNextLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line != null) {
            line = line.trim();
        }
        return line;
    }
    private int getTrailingInteger(String line) {
        return Integer.parseInt(line.split("=", 2)[1]);
    }
    
}
