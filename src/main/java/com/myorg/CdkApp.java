package com.myorg;

import software.amazon.awscdk.core.App;

public class CdkApp {
    public static void main(final String[] args) {
        App app = new App();

        VpcStack vpc = new VpcStack(app, "Vpc");

        ClusterStack cluster = new ClusterStack(app, "Cluster", vpc.getVpc());
        cluster.addDependency(vpc);

        RdsStack rdsStack = new RdsStack(app, "Rds", vpc.getVpc());
        rdsStack.addDependency(vpc);

        SnsStack snsStack = new SnsStack(app, "Sns");

        Service01Stack service01Stack = new Service01Stack(app, "Service01", cluster.getCluster(), snsStack.getProductEventsTopic());
        service01Stack.addDependency(cluster);
        service01Stack.addDependency(rdsStack);
        service01Stack.addDependency(snsStack);

        Service02Stack service02Stack = new Service02Stack(app, "Service02",cluster.getCluster(),snsStack.getProductEventsTopic());
        service02Stack.addDependency(snsStack);

        app.synth();
    }
}
