
#ifndef __HID_MONITOR_H__
#define __HID_MONITOR_H__
namespace HID {
	class HIDMonitor {
	public:
		static const int HID_ARRIVED = 1<<0;
		static const int HID_REMOVED = 1<<1;

		static bool initHIDMonitor();
		static int updateHIDMonitor();
		static void destroyHIDMonitor();
	private:
		HIDMonitor();
		~HIDMonitor();
		HIDMonitor(const HIDMonitor& other);
		HIDMonitor& operator=(const HIDMonitor& rhs);
	};
}
#endif

