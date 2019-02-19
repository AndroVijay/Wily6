package info.earntalktime.at.event;

public class CenterStateEvent {
    private int isOpen;

    public CenterStateEvent(int isOpen) {

        this.isOpen = isOpen;
    }

    public int isOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }
}
