package name.wydler.hudson.plugin.test.scm;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.scm.*;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.File;
import java.io.IOException;

public final class DelayedNullSCM extends SCM {

    private final Long sleep;

    @DataBoundConstructor
    public DelayedNullSCM(Long sleep) {
        this.sleep = sleep;
    }

    public Long getSleep() {
        return sleep;
    }

    @Override
    public SCMRevisionState calcRevisionsFromBuild(AbstractBuild<?, ?> build, hudson.Launcher launcher, TaskListener listener) throws IOException, InterruptedException {
        return null;
    }

    @Override
    protected PollingResult compareRemoteRevisionWith(AbstractProject<?, ?> project, hudson.Launcher launcher, FilePath workspace, TaskListener listener, SCMRevisionState baseline) throws IOException, InterruptedException {
        listener.getLogger().println("Sleep started for " + sleep + " seconds");
        Thread.sleep(sleep * 1000);
        listener.getLogger().println("Sleep ended");
        return PollingResult.NO_CHANGES;
    }

    @Override
    public boolean checkout(AbstractBuild<?, ?> build, hudson.Launcher launcher, FilePath workspace, BuildListener listener, File changelogFile) throws IOException, InterruptedException {
        return createEmptyChangeLog(changelogFile, listener, "log");
    }

    public ChangeLogParser createChangeLogParser() {
        return new NullChangeLogParser();
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends SCMDescriptor<NullSCM> {
        public DescriptorImpl() {
            super(null);
            load();
        }

        public String getDisplayName() {
            return "Delayed None SCM";
        }

        @Override
        public SCM newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return super.newInstance(req, formData);
        }
    }
}
