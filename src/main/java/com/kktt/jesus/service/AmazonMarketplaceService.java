package com.kktt.jesus.service;

import com.kktt.jesus.dao.source1.AmazonMarketplaceDao;
import com.kktt.jesus.dataobject.AmazonMarketplace;
import com.kktt.jesus.exception.MWSConnectedStatusException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AmazonMarketplaceService {

    @Resource
    private AmazonMarketplaceDao amazonMarketplaceDao;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Integer> findActiveIds() {
        return amazonMarketplaceDao.findActiveIds();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AmazonMarketplace> findWaiting() {
        return amazonMarketplaceDao.findWaiting();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AmazonMarketplace findById(Integer id) {
        return amazonMarketplaceDao.findById(id);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AmazonMarketplace forceFindActive(Integer id) throws MWSConnectedStatusException {
        AmazonMarketplace amazonMarketplace = findById(id);
        if (amazonMarketplace.getStatus() != 1)
            throw new MWSConnectedStatusException("Status: " + amazonMarketplace.getStatus(), amazonMarketplace);
        return amazonMarketplace;
    }

    public void update(String sellerId, String marketplaceId, int status) {
        amazonMarketplaceDao.update(sellerId, marketplaceId, status);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AmazonMarketplace find(String sellerId, String marketplaceId) {
        return amazonMarketplaceDao.findBySellerIdAndMarketplaceId(sellerId, marketplaceId);
    }
}