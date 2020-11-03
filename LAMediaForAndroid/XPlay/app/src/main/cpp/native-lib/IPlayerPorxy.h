
#ifndef XPLAY_IPLAYERPORXY_H
#define XPLAY_IPLAYERPORXY_H


#include "IPlayer.h"
#include <mutex>
class IPlayerPorxy: public IPlayer
{
public:
    static IPlayerPorxy*Get()
    {
        static IPlayerPorxy px;
        return &px;
    }
    void Init(void *vm = 0);

    virtual bool Open(const char *path);
    virtual bool Start();
    virtual void InitView(void *win);

protected:
    IPlayerPorxy(){}
    IPlayer *player = 0;
    std::mutex mux;
};


#endif //XPLAY_IPLAYERPORXY_H
