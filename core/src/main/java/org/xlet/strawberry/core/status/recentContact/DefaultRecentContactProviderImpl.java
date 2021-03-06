package org.xlet.strawberry.core.status.recentContact;

import org.xlet.strawberry.core.status.basicStatus.Status;
import org.xlet.strawberry.core.member.BasicMember;
import org.xlet.strawberry.core.status.basicStatus.DefaultStatusProvider;
import org.xlet.strawberry.core.status.basicStatus.StatusProvider;
import org.xlet.strawberry.core.spring.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * recent contact status provider default implement.
 */
public class DefaultRecentContactProviderImpl implements RecentContactProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRecentContactProviderImpl.class);

    /**
     * recent contact limit keep number.
     * default 100.
     */
    private static final int RECENT_CONTACT_MAX_LIMIT = 100;

    private CacheRecentContactStatusCollection cacheRecentContactStatus;

    private StatusProvider statusProvider;

    private RecentContactStatusPersistentProvider persistentProvider;


    public DefaultRecentContactProviderImpl() {
        this.cacheRecentContactStatus = new CacheRecentContactStatusCollection();
        this.statusProvider = new DefaultStatusProvider();
        this.persistentProvider = SpringContext.context().getBean(RecentContactStatusPersistentProvider.class);
    }

    @Override
    public RecentContactStatuses get(BasicMember owner) {

        //get stored status from db and init status cache.
        this.initCachedStatus(owner);

        SortedSet<RecentContactStatus> sortedRecentContactItems = this.getCachedSortedRecentContactItems(owner);
        if (sortedRecentContactItems.size() <= RECENT_CONTACT_MAX_LIMIT) {
            return new RecentContactStatuses(owner, sortedRecentContactItems);
        } else {
            int i = 1;
            List<RecentContactStatus> limitRecentContactItems = new ArrayList<RecentContactStatus>();
            for (RecentContactStatus recentContactItem : sortedRecentContactItems) {
                limitRecentContactItems.add(recentContactItem);
                if (i++ > RECENT_CONTACT_MAX_LIMIT) {
                    return new RecentContactStatuses(owner, limitRecentContactItems);
                }
            }
        }
        return new RecentContactStatuses(owner, new ArrayList<RecentContactStatus>());
    }

    private void initCachedStatus(BasicMember owner) {
        Collection<RecentContactStatus> statuses = this.persistentProvider.get(owner, RECENT_CONTACT_MAX_LIMIT);
        this.cacheRecentContactStatus.initOwnerCachedStatus(statuses);
    }


    @Override
    public void change(BasicMember owner, BasicMember contact, String messageContent) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("member[{}] <-> member[{}] status change! lastContent is {}.", owner.getId(), contact.getId(), messageContent);
        }
        this.cacheRecentContactStatus.statusChanged(owner, contact, System.currentTimeMillis(), messageContent);
    }

    @Override
    public void onMemberLogout(BasicMember member) {
        Collection<RecentContactStatus> hasPersistentItems = new ArrayList<RecentContactStatus>();
        Collection<RecentContactStatus> contactItems = this.getCachedSortedRecentContactItems(member);
        for (RecentContactStatus item : contactItems) {
            if (this.statusProvider.status(item.getContact()).getStatus() == Status.Offline) {
                hasPersistentItems.add(item);
                this.cacheRecentContactStatus.remove(item);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("get {} recent contact status who has persistent.", hasPersistentItems.size());
        }
        this.persistentProvider.save(hasPersistentItems);
    }


    private Comparator<RecentContactStatus> recentContactItemComparator = new Comparator<RecentContactStatus>() {
        @Override
        public int compare(RecentContactStatus contactItem, RecentContactStatus contactItem2) {
            if (contactItem.getLastContactTime() > contactItem.getLastContactTime()) return -1;
            return 1;
        }
    };

    private SortedSet<RecentContactStatus> getCachedSortedRecentContactItems(BasicMember owner) {
        Collection<RecentContactStatus> recentContactItems = this.cacheRecentContactStatus.getRecentContacts(owner);
        SortedSet<RecentContactStatus> recentContactItemSortedSet = new TreeSet<RecentContactStatus>(this.recentContactItemComparator);
        recentContactItemSortedSet.addAll(recentContactItems);
        return recentContactItemSortedSet;
    }
}
