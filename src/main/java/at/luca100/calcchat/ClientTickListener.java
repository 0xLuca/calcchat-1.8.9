package at.luca100.calcchat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientTickListener {
    private CalcChat calcChat;

    public ClientTickListener(CalcChat calcChat) {
        this.calcChat = calcChat;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        calcChat.handleTickEvent();
    }
}
