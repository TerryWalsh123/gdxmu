package com.gdxmu.demos.advanced;

// NOTE: Implement to associate GamePadDescriptor codes with your game logic.
// See com.gdxmu.demos.advanced.MyXbox360Normalizer for an example.
public interface GamePadNormalizer {
	int normalizeButton(int buttonCode);
	int normalizeAxis(int axisCode, float value);
}
