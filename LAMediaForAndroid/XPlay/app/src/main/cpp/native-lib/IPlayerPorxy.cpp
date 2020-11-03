

#include "IPlayerPorxy.h"
#include "FFPlayerBuilder.h"
#include "XLog.h"

void IPlayerPorxy::Init(void *vm)
{
    mux.lock();
    if(vm)
    {
        FFPlayerBuilder::InitHard(vm);
    }
    if(!player)
        player = FFPlayerBuilder::Get()->BuilderPlayer();

    mux.unlock();
}
bool IPlayerPorxy::Open(const char *path)
{

    bool re = false;
    mux.lock();
    if(player)

        re = player->Open(path);
    mux.unlock();
    return re;
}
bool IPlayerPorxy::Start()
{
    bool re = false;
    mux.lock();
    if(player) {
        re = player->Start();
    }
    mux.unlock();
    return re;
}
void IPlayerPorxy::InitView(void *win)
{
    mux.lock();
    if(player)
        player->InitView(win);
    mux.unlock();
}