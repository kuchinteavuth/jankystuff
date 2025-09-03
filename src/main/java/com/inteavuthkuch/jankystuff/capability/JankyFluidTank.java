package com.inteavuthkuch.jankystuff.capability;

import com.inteavuthkuch.jankystuff.common.FluidTankTier;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JankyFluidTank extends FluidTank {
    private final Runnable onChange;

    public JankyFluidTank(int capacity, @Nullable Runnable onChange) {
        super(capacity);
        this.onChange = onChange;
    }

    public JankyFluidTank(@NotNull FluidTankTier tier, @Nullable Runnable onChange) {
        super(tier.getCapacity());
        this.onChange = onChange;
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        if(onChange != null)
            onChange.run();
    }
}
