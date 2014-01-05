#include <com.gdxmu.controllers.Ois.h>

//@line:49

	#include <OISInputManager.h>
	JNIEXPORT void JNICALL Java_com_gdxmu_controllers_Ois_destroyInputManager(JNIEnv* env, jobject object, jlong inputManagerPtr) {


//@line:53

		OIS::InputManager* inputManager = (OIS::InputManager*)inputManagerPtr;
		OIS::InputManager::destroyInputSystem(inputManager);
	

}

