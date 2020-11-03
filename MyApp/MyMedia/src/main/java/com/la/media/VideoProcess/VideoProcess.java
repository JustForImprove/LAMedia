package com.la.media.VideoProcess;

public interface VideoProcess {
    interface Callback{

    }

    interface View{
        void processBegin();
        void processContinued(int progress);
        void processFinished();
    }

    interface Presenter{
        void onFunctionItemClick(int position);

        String selectFile();
    }

    interface Model{

    }

}
