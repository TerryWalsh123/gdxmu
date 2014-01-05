
#include "HIDMonitor.h"

using namespace HID;

#include <Cocoa/Cocoa.h>
#include <IOKit/hid/IOHIDManager.h>

static int deviceListChanges = 0;
static IOHIDManagerRef hidManager = NULL;
static const CFStringRef kHIDRunLoopMode = CFStringCreateWithCString(NULL, "kHIDRunLoopMode", kCFStringEncodingUTF8);

static void Handle_DeviceMatchingCallback(void *inContext, IOReturn inResult, void *inSender, IOHIDDeviceRef inIOHIDDeviceRef) {
	deviceListChanges |= HIDMonitor::HID_ARRIVED;
}

static void Handle_DeviceRemovalCallback(void *inContext, IOReturn inResult, void *inSender, IOHIDDeviceRef inIOHIDDeviceRef) {
	deviceListChanges |= HIDMonitor::HID_REMOVED;
}

bool HIDMonitor::initHIDMonitor() {
	if (hidManager)
		return true;

	NSAutoreleasePool* pool = [[NSAutoreleasePool alloc] init];
	IOReturn ioRet = kIOReturnError;
	hidManager = IOHIDManagerCreate(kCFAllocatorDefault, kIOHIDOptionsTypeNone);

	if (hidManager) {
		ioRet = IOHIDManagerOpen(hidManager, kIOHIDOptionsTypeNone);

		if (kIOReturnSuccess == ioRet) {
            		IOHIDManagerSetDeviceMatching(hidManager, NULL);
			IOHIDManagerRegisterDeviceMatchingCallback(hidManager, Handle_DeviceMatchingCallback, NULL);
			IOHIDManagerRegisterDeviceRemovalCallback(hidManager, Handle_DeviceRemovalCallback, NULL);
			IOHIDManagerScheduleWithRunLoop(hidManager, CFRunLoopGetCurrent(), kHIDRunLoopMode);
		} else {
			CFRelease(hidManager); hidManager = NULL;
		}
	}

	[pool drain];
	return kIOReturnSuccess == ioRet;
}

int HIDMonitor::updateHIDMonitor() {
	if (hidManager) {
		NSAutoreleasePool* pool = [[NSAutoreleasePool alloc] init];
		deviceListChanges = 0;
		CFTimeInterval seconds = 0;
		CFRunLoopRunInMode(kHIDRunLoopMode, seconds, true);
		[pool drain];
	}
	return deviceListChanges;
}

void HIDMonitor::destroyHIDMonitor() {
	if (hidManager) {
		NSAutoreleasePool* pool = [[NSAutoreleasePool alloc] init];
		IOHIDManagerUnscheduleFromRunLoop(hidManager, CFRunLoopGetCurrent(), kHIDRunLoopMode);
		IOHIDManagerClose(hidManager, kIOHIDOptionsTypeNone);
		CFRelease(hidManager);
		hidManager = NULL;
		deviceListChanges = 0;
		[pool drain];
	}	
}


