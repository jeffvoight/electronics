
//#include "IdeShortcuts.h"

class NetbeansIdeShortcuts : public IdeShortcuts //, public OtherExtendedClass
{
	public:

//		virtual
		KeyboardShortcut openType()
		{
			KeyboardShortcut openType;

			openType.keys[0] = 7;
			openType.keyCount = 1;

			// alt + shift AO+ o
			TrinketKeyboard.pressKey(KEYCODE_RIGHT_ALT, KEYCODE_RIGHT_SHIFT, KEYCODE_O);

			return openType;
		}
};