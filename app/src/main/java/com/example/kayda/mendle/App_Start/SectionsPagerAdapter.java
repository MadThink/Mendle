package com.example.kayda.mendle.App_Start;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kayda.mendle.Areas.Chat.Fragments.ChatsFragment;
import com.example.kayda.mendle.Areas.Contacts.Fragments.FriendsFragment;
import com.example.kayda.mendle.Areas.Requests.Fragments.RequestsFragment;

/**
 * Created by Kayda on 2/21/2018.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){

        case 0:
        RequestsFragment requestsFragment=new RequestsFragment();
        return requestsFragment;

        case 1:
                ChatsFragment chatsFragment=new ChatsFragment();
                return chatsFragment;

        case 2:
                FriendsFragment friendsFragment=new FriendsFragment();
                return friendsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch(position){
            case 0:
                return "Request";
            case 1:
                return "Chats";
            case 2:
                return "Friends";
            default:
                return null;
        }
    }
    }


