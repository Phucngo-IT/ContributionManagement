package spring.boot.contributionmanagement.services;

import spring.boot.contributionmanagement.entities.LogDownload;

import java.util.List;

public interface LogDownloadService {
    public List<LogDownload> findAll();

    public LogDownload findById(Long id);

    public void saveAndUpdate(LogDownload logDownload);

    public void deleteById(Long id);
}
