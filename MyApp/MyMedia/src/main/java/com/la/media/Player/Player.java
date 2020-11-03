package com.la.media.Player;

public interface Player {

    interface PlayerCallBack{
        void back();
    }

    interface View{
        void lock_or_unlock_Screen();
        void show_or_hide_Screen();
        void play_or_pause();
        void setPlayUrl(String url);

    }

    interface Presenter{
        
    }

    interface Model{

    }

}
