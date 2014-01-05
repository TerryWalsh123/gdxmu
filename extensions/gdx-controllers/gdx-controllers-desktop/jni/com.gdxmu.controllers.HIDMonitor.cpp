#include <com.gdxmu.controllers.HIDMonitor.h>

//@line:53

	#include <HIDMonitor.h>
	JNIEXPORT jboolean JNICALL Java_com_gdxmu_controllers_HIDMonitor_initHIDMonitor(JNIEnv* env, jobject object) {


//@line:57

		return HID::HIDMonitor::initHIDMonitor();
	

}

JNIEXPORT jint JNICALL Java_com_gdxmu_controllers_HIDMonitor_updateHIDMonitor(JNIEnv* env, jobject object) {


//@line:61

		return HID::HIDMonitor::updateHIDMonitor();
	

}

JNIEXPORT void JNICALL Java_com_gdxmu_controllers_HIDMonitor_destroyHIDMonitor(JNIEnv* env, jobject object) {


//@line:65

		HID::HIDMonitor::destroyHIDMonitor();
	

}

